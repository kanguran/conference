import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventRegistrationComponent } from './list/event-registration.component';
import { EventRegistrationDetailComponent } from './detail/event-registration-detail.component';
import { EventRegistrationUpdateComponent } from './update/event-registration-update.component';
import { EventRegistrationDeleteDialogComponent } from './delete/event-registration-delete-dialog.component';
import { EventRegistrationRoutingModule } from './route/event-registration-routing.module';

@NgModule({
  imports: [SharedModule, EventRegistrationRoutingModule],
  declarations: [
    EventRegistrationComponent,
    EventRegistrationDetailComponent,
    EventRegistrationUpdateComponent,
    EventRegistrationDeleteDialogComponent,
  ],
})
export class EventRegistrationModule {}
