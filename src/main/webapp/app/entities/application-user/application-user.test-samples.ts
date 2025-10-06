import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 15785,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 18666,
};

export const sampleWithFullData: IApplicationUser = {
  id: 9548,
  host: true,
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
