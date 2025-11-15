import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../event-context.test-samples';

import { EventContextFormService } from './event-context-form.service';

describe('EventContext Form Service', () => {
  let service: EventContextFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventContextFormService);
  });

  describe('Service methods', () => {
    describe('createEventContextFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventContextFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            eventContextStatus: expect.any(Object),
            start: expect.any(Object),
            end: expect.any(Object),
            eventContextRoom: expect.any(Object),
            contextHost: expect.any(Object),
            event: expect.any(Object),
          }),
        );
      });

      it('passing IEventContext should create a new form with FormGroup', () => {
        const formGroup = service.createEventContextFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            eventContextStatus: expect.any(Object),
            start: expect.any(Object),
            end: expect.any(Object),
            eventContextRoom: expect.any(Object),
            contextHost: expect.any(Object),
            event: expect.any(Object),
          }),
        );
      });
    });

    describe('getEventContext', () => {
      it('should return NewEventContext for default EventContext initial value', () => {
        const formGroup = service.createEventContextFormGroup(sampleWithNewData);

        const eventContext = service.getEventContext(formGroup) as any;

        expect(eventContext).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventContext for empty EventContext initial value', () => {
        const formGroup = service.createEventContextFormGroup();

        const eventContext = service.getEventContext(formGroup) as any;

        expect(eventContext).toMatchObject({});
      });

      it('should return IEventContext', () => {
        const formGroup = service.createEventContextFormGroup(sampleWithRequiredData);

        const eventContext = service.getEventContext(formGroup) as any;

        expect(eventContext).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventContext should not enable id FormControl', () => {
        const formGroup = service.createEventContextFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventContext should disable id FormControl', () => {
        const formGroup = service.createEventContextFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
