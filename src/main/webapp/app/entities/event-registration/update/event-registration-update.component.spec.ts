import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IEventContext } from 'app/entities/event-context/event-context.model';
import { EventContextService } from 'app/entities/event-context/service/event-context.service';
import { IEventRegistration } from '../event-registration.model';
import { EventRegistrationService } from '../service/event-registration.service';
import { EventRegistrationFormService } from './event-registration-form.service';

import { EventRegistrationUpdateComponent } from './event-registration-update.component';

describe('EventRegistration Management Update Component', () => {
  let comp: EventRegistrationUpdateComponent;
  let fixture: ComponentFixture<EventRegistrationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventRegistrationFormService: EventRegistrationFormService;
  let eventRegistrationService: EventRegistrationService;
  let applicationUserService: ApplicationUserService;
  let eventContextService: EventContextService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EventRegistrationUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EventRegistrationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventRegistrationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventRegistrationFormService = TestBed.inject(EventRegistrationFormService);
    eventRegistrationService = TestBed.inject(EventRegistrationService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    eventContextService = TestBed.inject(EventContextService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ApplicationUser query and add missing value', () => {
      const eventRegistration: IEventRegistration = { id: 29825 };
      const eventCounterparty: IApplicationUser = { id: 2107 };
      eventRegistration.eventCounterparty = eventCounterparty;

      const applicationUserCollection: IApplicationUser[] = [{ id: 2107 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [eventCounterparty];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventRegistration });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining),
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('should call EventContext query and add missing value', () => {
      const eventRegistration: IEventRegistration = { id: 29825 };
      const eventContext: IEventContext = { id: 18286 };
      eventRegistration.eventContext = eventContext;

      const eventContextCollection: IEventContext[] = [{ id: 18286 }];
      jest.spyOn(eventContextService, 'query').mockReturnValue(of(new HttpResponse({ body: eventContextCollection })));
      const additionalEventContexts = [eventContext];
      const expectedCollection: IEventContext[] = [...additionalEventContexts, ...eventContextCollection];
      jest.spyOn(eventContextService, 'addEventContextToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventRegistration });
      comp.ngOnInit();

      expect(eventContextService.query).toHaveBeenCalled();
      expect(eventContextService.addEventContextToCollectionIfMissing).toHaveBeenCalledWith(
        eventContextCollection,
        ...additionalEventContexts.map(expect.objectContaining),
      );
      expect(comp.eventContextsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const eventRegistration: IEventRegistration = { id: 29825 };
      const eventCounterparty: IApplicationUser = { id: 2107 };
      eventRegistration.eventCounterparty = eventCounterparty;
      const eventContext: IEventContext = { id: 18286 };
      eventRegistration.eventContext = eventContext;

      activatedRoute.data = of({ eventRegistration });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContainEqual(eventCounterparty);
      expect(comp.eventContextsSharedCollection).toContainEqual(eventContext);
      expect(comp.eventRegistration).toEqual(eventRegistration);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventRegistration>>();
      const eventRegistration = { id: 3555 };
      jest.spyOn(eventRegistrationFormService, 'getEventRegistration').mockReturnValue(eventRegistration);
      jest.spyOn(eventRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventRegistration }));
      saveSubject.complete();

      // THEN
      expect(eventRegistrationFormService.getEventRegistration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventRegistrationService.update).toHaveBeenCalledWith(expect.objectContaining(eventRegistration));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventRegistration>>();
      const eventRegistration = { id: 3555 };
      jest.spyOn(eventRegistrationFormService, 'getEventRegistration').mockReturnValue({ id: null });
      jest.spyOn(eventRegistrationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventRegistration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventRegistration }));
      saveSubject.complete();

      // THEN
      expect(eventRegistrationFormService.getEventRegistration).toHaveBeenCalled();
      expect(eventRegistrationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventRegistration>>();
      const eventRegistration = { id: 3555 };
      jest.spyOn(eventRegistrationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventRegistration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventRegistrationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('should forward to applicationUserService', () => {
        const entity = { id: 2107 };
        const entity2 = { id: 4268 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEventContext', () => {
      it('should forward to eventContextService', () => {
        const entity = { id: 18286 };
        const entity2 = { id: 3213 };
        jest.spyOn(eventContextService, 'compareEventContext');
        comp.compareEventContext(entity, entity2);
        expect(eventContextService.compareEventContext).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
