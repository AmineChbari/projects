import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ReservationService } from '../../core/services/reservation';
import { ROOMS, ROOM_NAMES } from '../../core/data/rooms.data';
import { Room } from '../../core/models/reservation.model';

@Component({
  selector: 'app-book',
  standalone: false,
  templateUrl: './book.html',
  styleUrl: './book.scss',
})
export class Book implements OnInit {
  step = 1;
  rooms: Room[] = ROOMS;
  selectedRoom: Room | null = null;
  form!: FormGroup;
  loading = false;
  success = false;
  error = '';
  bookingId = 0;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private svc: ReservationService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      taille:       [1, [Validators.required, Validators.min(1), Validators.max(4)]],
      nom:          ['', [Validators.required, Validators.minLength(2)]],
      sejour:       [1, [Validators.required, Validators.min(1), Validators.max(10)]],
      dateArrivee:  ['', Validators.required],
      commentaire:  ['', Validators.maxLength(120)],
    });

    this.route.queryParams.subscribe(p => {
      const t = parseInt(p['taille']);
      if (t >= 1 && t <= 4) {
        this.form.patchValue({ taille: t });
        this.selectRoom(t);
        this.step = 2;
      }
    });
  }

  selectRoom(taille: number) {
    this.selectedRoom = this.rooms.find(r => r.taille === taille) || null;
    this.form.patchValue({ taille });
    this.step = 2;
  }

  get f() { return this.form.controls; }

  get totalPrice(): number {
    return this.selectedRoom ? this.selectedRoom.prix * (this.form.value.sejour || 1) : 0;
  }

  submit() {
    if (this.form.invalid || !this.selectedRoom) return;
    this.loading = true;
    this.error = '';
    const payload = {
      ...this.form.value,
      chambre: ROOM_NAMES[this.form.value.taille]
    };
    this.svc.create(payload).subscribe({
      next: (res) => {
        this.bookingId = res.id;
        this.loading = false;
        this.success = true;
        this.step = 3;
      },
      error: () => {
        this.loading = false;
        this.error = 'Une erreur est survenue. Veuillez réessayer.';
      }
    });
  }

  reset() {
    this.step = 1;
    this.success = false;
    this.selectedRoom = null;
    this.form.reset({ taille: 1, sejour: 1 });
  }

  goToReservations() {
    this.router.navigate(['/reservations']);
  }

  minDate(): string {
    return new Date().toISOString().split('T')[0];
  }
}
