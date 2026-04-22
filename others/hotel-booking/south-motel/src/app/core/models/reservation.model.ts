export interface Reservation {
  id: number;
  nom: string;
  taille: number;
  sejour: number;
  commentaire: string;
  chambre?: string;
  dateArrivee?: string;
}

export interface Room {
  id: number;
  taille: number;
  nom: string;
  description: string;
  prix: number;
  capacite: number;
  surface: number;
  amenities: string[];
  image: string;
  tag?: string;
}

export interface ReservationStats {
  taille: number;
  count: number;
  avgDuration: number;
}
