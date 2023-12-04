import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {
  loading = true;

  showLoader() {
    this.loading = true;
  }

  hideLoader() {
    this.loading = false;
  }
}
