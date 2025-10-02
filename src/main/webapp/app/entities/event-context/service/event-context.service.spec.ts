import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEventContext } from '../event-context.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../event-context.test-samples';

import { EventContextService, RestEventContext } from './event-context.service';

const requireRestSample: RestEventContext = {
  ...sampleWithRequiredData,
  start: sampleWithRequiredData.start?.toJSON(),
  end: sampleWithRequiredData.end?.toJSON(),
};

describe('EventContext Service', () => {
  let service: EventContextService;
  let httpMock: HttpTestingController;
  let expectedResult: IEventContext | IEventContext[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventContextService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EventContext', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const eventContext = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(eventContext).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventContext', () => {
      const eventContext = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(eventContext).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EventContext', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventContext', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EventContext', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEventContextToCollectionIfMissing', () => {
      it('should add a EventContext to an empty array', () => {
        const eventContext: IEventContext = sampleWithRequiredData;
        expectedResult = service.addEventContextToCollectionIfMissing([], eventContext);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventContext);
      });

      it('should not add a EventContext to an array that contains it', () => {
        const eventContext: IEventContext = sampleWithRequiredData;
        const eventContextCollection: IEventContext[] = [
          {
            ...eventContext,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEventContextToCollectionIfMissing(eventContextCollection, eventContext);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventContext to an array that doesn't contain it", () => {
        const eventContext: IEventContext = sampleWithRequiredData;
        const eventContextCollection: IEventContext[] = [sampleWithPartialData];
        expectedResult = service.addEventContextToCollectionIfMissing(eventContextCollection, eventContext);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventContext);
      });

      it('should add only unique EventContext to an array', () => {
        const eventContextArray: IEventContext[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const eventContextCollection: IEventContext[] = [sampleWithRequiredData];
        expectedResult = service.addEventContextToCollectionIfMissing(eventContextCollection, ...eventContextArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventContext: IEventContext = sampleWithRequiredData;
        const eventContext2: IEventContext = sampleWithPartialData;
        expectedResult = service.addEventContextToCollectionIfMissing([], eventContext, eventContext2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventContext);
        expect(expectedResult).toContain(eventContext2);
      });

      it('should accept null and undefined values', () => {
        const eventContext: IEventContext = sampleWithRequiredData;
        expectedResult = service.addEventContextToCollectionIfMissing([], null, eventContext, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventContext);
      });

      it('should return initial array if no EventContext is added', () => {
        const eventContextCollection: IEventContext[] = [sampleWithRequiredData];
        expectedResult = service.addEventContextToCollectionIfMissing(eventContextCollection, undefined, null);
        expect(expectedResult).toEqual(eventContextCollection);
      });
    });

    describe('compareEventContext', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEventContext(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEventContext(entity1, entity2);
        const compareResult2 = service.compareEventContext(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEventContext(entity1, entity2);
        const compareResult2 = service.compareEventContext(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEventContext(entity1, entity2);
        const compareResult2 = service.compareEventContext(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
