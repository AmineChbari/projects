import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ROOMS } from '../../core/data/rooms.data';
import { Room } from '../../core/models/reservation.model';

@Component({
  selector: 'app-rooms',
  standalone: false,
  templateUrl: './rooms.html',
  styleUrl: './rooms.scss',
})
export class Rooms {
  rooms: Room[] = ROOMS;

  constructor(private router: Router) {}

  book(taille: number) {
    this.router.navigate(['/book'], { queryParams: { taille } });
  }
}
