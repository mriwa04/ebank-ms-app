import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loadingSignal = signal<boolean>(false);
  isLoading = this.loadingSignal.asReadonly();

  setLoading(value: boolean) {
    this.loadingSignal.set(value);
  }
}
