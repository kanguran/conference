import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEventContext, NewEventContext } from '../event-context.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventContext for edit and NewEventContextFormGroupInput for create.
 */
type EventContextFormGroupInput = IEventContext | PartialWithRequiredKeyOf<NewEventContext>;

type EventContextFormDefaults = Pick<NewEventContext, 'id'>;

type EventContextFormGroupContent = {
  id: FormControl<IEventContext['id'] | NewEventContext['id']>;
  name: FormControl<IEventContext['name']>;
  eventContextStatus: FormControl<IEventContext['eventContextStatus']>;
  start: FormControl<IEventContext['start']>;
  end: FormControl<IEventContext['end']>;
  event: FormControl<IEventContext['event']>;
};

export type EventContextFormGroup = FormGroup<EventContextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventContextFormService {
  createEventContextFormGroup(eventContext: EventContextFormGroupInput = { id: null }): EventContextFormGroup {
    const eventContextRawValue = {
      ...this.getFormDefaults(),
      ...eventContext,
    };
    return new FormGroup<EventContextFormGroupContent>({
      id: new FormControl(
        { value: eventContextRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(eventContextRawValue.name, {
        validators: [Validators.required],
      }),
      eventContextStatus: new FormControl(eventContextRawValue.eventContextStatus),
      start: new FormControl(eventContextRawValue.start),
      end: new FormControl(eventContextRawValue.end),
      event: new FormControl(eventContextRawValue.event),
    });
  }

  getEventContext(form: EventContextFormGroup): IEventContext | NewEventContext {
    return form.getRawValue() as IEventContext | NewEventContext;
  }

  resetForm(form: EventContextFormGroup, eventContext: EventContextFormGroupInput): void {
    const eventContextRawValue = { ...this.getFormDefaults(), ...eventContext };
    form.reset(
      {
        ...eventContextRawValue,
        id: { value: eventContextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventContextFormDefaults {
    return {
      id: null,
    };
  }
}
