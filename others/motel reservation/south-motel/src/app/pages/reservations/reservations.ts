import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReservationService } from '../../core/services/reservation';
import { Reservation, ReservationStats } from '../../core/models/reservation.model';
import { ROOM_NAMES } from '../../core/data/rooms.data';

@Component({
  selector: 'app-reservations',
  standalone: false,
  templateUrl: './reservations.html',
  styleUrl: './reservations.scss',
})
export class Reservations implements OnInit {
  reservations: Reservation[] = [];
  filtered: Reservation[] = [];
  activeFilter = 0;
  editId: number | null = null;
  editForm!: FormGroup;
  loading = true;
  deleteId: number | null = null;
  alert: { type: 'success' | 'error'; msg: string } | null = null;
  roomNames = ROOM_NAMES;

  constructor(private svc: ReservationService, private fb: FormBuilder) {}

  ngOnInit() {
    this.editForm = this.fb.group({
      nom:         ['', [Validators.required, Validators.minLength(2)]],
      taille:      [1, [Validators.required, Validators.min(1), Validators.max(4)]],
      sejour:      [1, [Validators.required, Validators.min(1), Validators.max(10)]],
      dateArrivee: [''],
      commentaire: ['', Validators.maxLength(120)],
    });
    this.load();
  }

  load() {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => {
        this.reservations = data;
        this.applyFilter(this.activeFilter);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(taille: number) {
    this.activeFilter = taille;
    this.filtered = taille === 0
      ? this.reservations
      : this.reservations.filter(r => r.taille === taille);
  }

  startEdit(r: Reservation) {
    this.editId = r.id;
    this.editForm.patchValue({
      nom: r.nom, taille: r.taille, sejour: r.sejour,
      dateArrivee: r.dateArrivee || '', commentaire: r.commentaire || ''
    });
  }

  cancelEdit() { this.editId = null; }

  saveEdit(r: Reservation) {
    if (this.editForm.invalid) return;
    const payload = {
      ...this.editForm.value,
      chambre: ROOM_NAMES[this.editForm.value.taille]
    };
    this.svc.update(r.id, payload).subscribe({
      next: () => {
        this.editId = null;
        this.showAlert('success', 'Réservation mise à jour avec succès.');
        this.load();
      },
      error: () => this.showAlert('error', 'Erreur lors de la mise à jour.')
    });
  }

  confirmDelete(id: number) { this.deleteId = id; }
  cancelDelete() { this.deleteId = null; }

  doDelete() {
    if (this.deleteId === null) return;
    this.svc.delete(this.deleteId).subscribe({
      next: () => {
        this.deleteId = null;
        this.showAlert('success', 'Réservation supprimée.');
        this.load();
      },
      error: () => this.showAlert('error', 'Erreur lors de la suppression.')
    });
  }

  get stats(): ReservationStats[] {
    return [1, 2, 3, 4].map(t => {
      const list = this.reservations.filter(r => r.taille === t);
      const total = list.reduce((s, r) => s + r.sejour, 0);
      return { taille: t, count: list.length, avgDuration: list.length ? +(total / list.length).toFixed(1) : 0 };
    });
  }

  showAlert(type: 'success' | 'error', msg: string) {
    this.alert = { type, msg };
    setTimeout(() => this.alert = null, 3500);
  }
}
