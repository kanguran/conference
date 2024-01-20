import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEventContext | NewEventContext> = Omit<T, 'start' | 'end'> & {
  start?: string | null;
  end?: string | null;
};

type EventContextFormRawValue = FormValueOf<IEventContext>;

type NewEventContextFormRawValue = FormValueOf<NewEventContext>;

type EventContextFormDefaults = Pick<NewEventContext, 'id' | 'start' | 'end'>;

type EventContextFormGroupContent = {
  id: FormControl<EventContextFormRawValue['id'] | NewEventContext['id']>;
  name: FormControl<EventContextFormRawValue['name']>;
  eventContextStatus: FormControl<EventContextFormRawValue['eventContextStatus']>;
  start: FormControl<EventContextFormRawValue['start']>;
  end: FormControl<EventContextFormRawValue['end']>;
  contextHost: FormControl<EventContextFormRawValue['contextHost']>;
  event: FormControl<EventContextFormRawValue['event']>;
};

export type EventContextFormGroup = FormGroup<EventContextFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventContextFormService {
  createEventContextFormGroup(eventContext: EventContextFormGroupInput = { id: null }): EventContextFormGroup {
    const eventContextRawValue = this.convertEventContextToEventContextRawValue({
      ...this.getFormDefaults(),
      ...eventContext,
    });
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
      contextHost: new FormControl(eventContextRawValue.contextHost),
      event: new FormControl(eventContextRawValue.event),
    });
  }

  getEventContext(form: EventContextFormGroup): IEventContext | NewEventContext {
    return this.convertEventContextRawValueToEventContext(form.getRawValue() as EventContextFormRawValue | NewEventContextFormRawValue);
  }

  resetForm(form: EventContextFormGroup, eventContext: EventContextFormGroupInput): void {
    const eventContextRawValue = this.convertEventContextToEventContextRawValue({ ...this.getFormDefaults(), ...eventContext });
    form.reset(
      {
        ...eventContextRawValue,
        id: { value: eventContextRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventContextFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      start: currentTime,
      end: currentTime,
    };
  }

  private convertEventContextRawValueToEventContext(
    rawEventContext: EventContextFormRawValue | NewEventContextFormRawValue
  ): IEventContext | NewEventContext {
    return {
      ...rawEventContext,
      start: dayjs(rawEventContext.start, DATE_TIME_FORMAT),
      end: dayjs(rawEventContext.end, DATE_TIME_FORMAT),
    };
  }

  private convertEventContextToEventContextRawValue(
    eventContext: IEventContext | (Partial<NewEventContext> & EventContextFormDefaults)
  ): EventContextFormRawValue | PartialWithRequiredKeyOf<NewEventContextFormRawValue> {
    return {
      ...eventContext,
      start: eventContext.start ? eventContext.start.format(DATE_TIME_FORMAT) : undefined,
      end: eventContext.end ? eventContext.end.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
