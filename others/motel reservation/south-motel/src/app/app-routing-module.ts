import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Rooms } from './pages/rooms/rooms';
import { Book } from './pages/book/book';
import { Reservations } from './pages/reservations/reservations';

const routes: Routes = [
  { path: '',             component: Home },
  { path: 'rooms',        component: Rooms },
  { path: 'book',         component: Book },
  { path: 'reservations', component: Reservations },
  { path: '**',           redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
