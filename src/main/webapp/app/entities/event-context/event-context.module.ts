import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventContextComponent } from './list/event-context.component';
import { EventContextDetailComponent } from './detail/event-context-detail.component';
import { EventContextUpdateComponent } from './update/event-context-update.component';
import { EventContextDeleteDialogComponent } from './delete/event-context-delete-dialog.component';
import { EventContextRoutingModule } from './route/event-context-routing.module';

@NgModule({
  imports: [SharedModule, EventContextRoutingModule],
  declarations: [EventContextComponent, EventContextDetailComponent, EventContextUpdateComponent, EventContextDeleteDialogComponent],
})
export class EventContextModule {}
