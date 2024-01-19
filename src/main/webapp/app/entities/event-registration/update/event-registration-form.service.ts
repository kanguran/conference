import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEventRegistration, NewEventRegistration } from '../event-registration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventRegistration for edit and NewEventRegistrationFormGroupInput for create.
 */
type EventRegistrationFormGroupInput = IEventRegistration | PartialWithRequiredKeyOf<NewEventRegistration>;

type EventRegistrationFormDefaults = Pick<NewEventRegistration, 'id'>;

type EventRegistrationFormGroupContent = {
  id: FormControl<IEventRegistration['id'] | NewEventRegistration['id']>;
  name: FormControl<IEventRegistration['name']>;
  eventRegistrationStatus: FormControl<IEventRegistration['eventRegistrationStatus']>;
  eventContext: FormControl<IEventRegistration['eventContext']>;
};

export type EventRegistrationFormGroup = FormGroup<EventRegistrationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventRegistrationFormService {
  createEventRegistrationFormGroup(eventRegistration: EventRegistrationFormGroupInput = { id: null }): EventRegistrationFormGroup {
    const eventRegistrationRawValue = {
      ...this.getFormDefaults(),
      ...eventRegistration,
    };
    return new FormGroup<EventRegistrationFormGroupContent>({
      id: new FormControl(
        { value: eventRegistrationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(eventRegistrationRawValue.name, {
        validators: [Validators.required],
      }),
      eventRegistrationStatus: new FormControl(eventRegistrationRawValue.eventRegistrationStatus),
      eventContext: new FormControl(eventRegistrationRawValue.eventContext),
    });
  }

  getEventRegistration(form: EventRegistrationFormGroup): IEventRegistration | NewEventRegistration {
    return form.getRawValue() as IEventRegistration | NewEventRegistration;
  }

  resetForm(form: EventRegistrationFormGroup, eventRegistration: EventRegistrationFormGroupInput): void {
    const eventRegistrationRawValue = { ...this.getFormDefaults(), ...eventRegistration };
    form.reset(
      {
        ...eventRegistrationRawValue,
        id: { value: eventRegistrationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventRegistrationFormDefaults {
    return {
      id: null,
    };
  }
}
