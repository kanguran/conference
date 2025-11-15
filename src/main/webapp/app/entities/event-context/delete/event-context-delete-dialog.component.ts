import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';

@Component({
  templateUrl: './event-context-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EventContextDeleteDialogComponent {
  eventContext?: IEventContext;

  protected eventContextService = inject(EventContextService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventContextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
