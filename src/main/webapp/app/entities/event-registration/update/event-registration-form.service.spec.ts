import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../event-registration.test-samples';

import { EventRegistrationFormService } from './event-registration-form.service';

describe('EventRegistration Form Service', () => {
  let service: EventRegistrationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventRegistrationFormService);
  });

  describe('Service methods', () => {
    describe('createEventRegistrationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventRegistrationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            eventRegistrationStatus: expect.any(Object),
            eventCounterparty: expect.any(Object),
            eventContext: expect.any(Object),
          }),
        );
      });

      it('passing IEventRegistration should create a new form with FormGroup', () => {
        const formGroup = service.createEventRegistrationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            eventRegistrationStatus: expect.any(Object),
            eventCounterparty: expect.any(Object),
            eventContext: expect.any(Object),
          }),
        );
      });
    });

    describe('getEventRegistration', () => {
      it('should return NewEventRegistration for default EventRegistration initial value', () => {
        const formGroup = service.createEventRegistrationFormGroup(sampleWithNewData);

        const eventRegistration = service.getEventRegistration(formGroup) as any;

        expect(eventRegistration).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventRegistration for empty EventRegistration initial value', () => {
        const formGroup = service.createEventRegistrationFormGroup();

        const eventRegistration = service.getEventRegistration(formGroup) as any;

        expect(eventRegistration).toMatchObject({});
      });

      it('should return IEventRegistration', () => {
        const formGroup = service.createEventRegistrationFormGroup(sampleWithRequiredData);

        const eventRegistration = service.getEventRegistration(formGroup) as any;

        expect(eventRegistration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventRegistration should not enable id FormControl', () => {
        const formGroup = service.createEventRegistrationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventRegistration should disable id FormControl', () => {
        const formGroup = service.createEventRegistrationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
