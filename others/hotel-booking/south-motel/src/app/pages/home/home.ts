import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ROOMS } from '../../core/data/rooms.data';
import { Room } from '../../core/models/reservation.model';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  featuredRooms: Room[] = ROOMS.slice(1, 4);

  amenities = [
    { icon: '🍽️', label: 'Restaurant gastronomique', desc: 'Cuisine marocaine & internationale' },
    { icon: '💆', label: 'Spa & Bien-être', desc: 'Massages, hammam, soins du corps' },
    { icon: '🏊', label: 'Piscine & Fitness', desc: 'Espace aquatique & salle de sport' },
    { icon: '🤝', label: 'Conciergerie 24h/24', desc: 'À votre service en permanence' },
  ];

  constructor(private router: Router) {}

  book(taille: number) {
    this.router.navigate(['/book'], { queryParams: { taille } });
  }
}
