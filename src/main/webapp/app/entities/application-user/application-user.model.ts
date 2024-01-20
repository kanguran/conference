import { IUser } from 'app/entities/user/user.model';

export interface IApplicationUser {
  id: number;
  host?: boolean | null;
  appUser?: Pick<IUser, 'id'> | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
