import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'event',
        data: { pageTitle: 'Events' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'event-context',
        data: { pageTitle: 'EventContexts' },
        loadChildren: () => import('./event-context/event-context.module').then(m => m.EventContextModule),
      },
      {
        path: 'event-registration',
        data: { pageTitle: 'EventRegistrations' },
        loadChildren: () => import('./event-registration/event-registration.module').then(m => m.EventRegistrationModule),
      },
      {
        path: 'room',
        data: { pageTitle: 'Rooms' },
        loadChildren: () => import('./room/room.module').then(m => m.RoomModule),
      },
      {
        path: 'application-user',
        data: { pageTitle: 'ApplicationUsers' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
