import { Room } from '../models/reservation.model';

export const ROOMS: Room[] = [
  {
    id: 1,
    taille: 1,
    nom: 'Chambre Standard',
    description: 'Élégante chambre individuelle baignée de lumière naturelle, avec vue sur les jardins intérieurs. Un refuge intime pour le voyageur solitaire en quête de sérénité.',
    prix: 89,
    capacite: 1,
    surface: 22,
    tag: 'Populaire',
    amenities: ['Wi-Fi haut débit', 'TV 4K', 'Climatisation', 'Mini-bar', 'Coffre-fort', 'Sèche-cheveux'],
    image: 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=700&q=80'
  },
  {
    id: 2,
    taille: 2,
    nom: 'Chambre Double',
    description: 'Spacieuse chambre pour deux, décorée avec raffinement. Lit king-size, salle de bain en marbre et balcon privatif avec vue panoramique sur la ville.',
    prix: 139,
    capacite: 2,
    surface: 32,
    amenities: ['Wi-Fi haut débit', 'TV 4K', 'Climatisation', 'Mini-bar', 'Balcon privé', 'Baignoire balnéo', 'Peignoirs & chaussons'],
    image: 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=700&q=80'
  },
  {
    id: 3,
    taille: 3,
    nom: 'Suite Junior',
    description: 'Une suite lumineuse combinant salon séparé et chambre raffinée pour accueillir trois personnes dans le plus grand confort. Idéale pour les familles ou les collègues.',
    prix: 199,
    capacite: 3,
    surface: 48,
    tag: 'Coup de cœur',
    amenities: ['Wi-Fi haut débit', 'TV 4K x2', 'Climatisation multi-zone', 'Bar privé', 'Salon séparé', 'Jacuzzi', 'Service en chambre 24h/24'],
    image: 'https://images.unsplash.com/photo-1560185893-a55cbc8c57e8?w=700&q=80'
  },
  {
    id: 4,
    taille: 4,
    nom: 'Suite Prestige',
    description: 'Le summum du luxe. Deux chambres, grand salon, cuisine américaine et terrasse privée. Un appartement dans l\'hôtel, pour une expérience inoubliable à quatre.',
    prix: 299,
    capacite: 4,
    surface: 75,
    tag: 'Exclusif',
    amenities: ['Wi-Fi fibre dédiée', 'Home cinéma', 'Climatisation', 'Cuisine américaine', 'Terrasse privée', 'Piscine privée', 'Butler personnel', 'Transfert aéroport'],
    image: 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=700&q=80'
  }
];

export const ROOM_NAMES: Record<number, string> = {
  1: 'Chambre Standard',
  2: 'Chambre Double',
  3: 'Suite Junior',
  4: 'Suite Prestige'
};
