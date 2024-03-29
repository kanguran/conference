import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventContext } from '../event-context.model';
import { EventContextService } from '../service/event-context.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './event-context-delete-dialog.component.html',
})
export class EventContextDeleteDialogComponent {
  eventContext?: IEventContext;

  constructor(protected eventContextService: EventContextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventContextService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
