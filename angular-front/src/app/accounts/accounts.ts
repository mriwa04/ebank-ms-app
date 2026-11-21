import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AsyncPipe, DatePipe, DecimalPipe } from '@angular/common';
import { catchError, map, Observable, of } from 'rxjs';
import { Account, AccountListState, RequestStatus } from '../model/account.model';

@Component({
  selector: 'app-accounts',
  imports: [AsyncPipe, DatePipe, DecimalPipe],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements OnInit {
  private http = inject(HttpClient);
  protected readonly RequestStatus = RequestStatus;
  accountsState$!: Observable<AccountListState>;

  ngOnInit(): void {
    this.accountsState$ = this.http
      .get<Account[]>('http://localhost:9999/EBANK-SERVICES/accounts')
      .pipe(
        map((accounts) => ({
          accounts,
          status: RequestStatus.SUCCESS
        })),
        catchError((err) =>
          of({
            status: RequestStatus.ERROR,
            errorMessage: err.statusText || err.message || 'Erreur lors du chargement des comptes'
          })
        )
      );
  }
}
