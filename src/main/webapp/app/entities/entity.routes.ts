import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'event',
    data: { pageTitle: 'Events' },
    loadChildren: () => import('./event/event.routes'),
  },
  {
    path: 'event-context',
    data: { pageTitle: 'EventContexts' },
    loadChildren: () => import('./event-context/event-context.routes'),
  },
  {
    path: 'event-registration',
    data: { pageTitle: 'EventRegistrations' },
    loadChildren: () => import('./event-registration/event-registration.routes'),
  },
  {
    path: 'room',
    data: { pageTitle: 'Rooms' },
    loadChildren: () => import('./room/room.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'ApplicationUsers' },
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
