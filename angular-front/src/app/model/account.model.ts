export enum RequestStatus {
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR'
}

export interface Account {
  id: string;
  balance: number;
  createdAt?: string;
  type: string;
  customerId?: number;
  customer?: any;
}

export interface AccountListState {
  accounts?: Account[];
  status?: RequestStatus;
  errorMessage?: string;
}
