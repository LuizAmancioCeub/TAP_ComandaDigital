import { Injectable } from '@angular/core';
import { CredencialsData } from '../Models/CredencialsData';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userData:CredencialsData|null = null;
  constructor() {}

  setUserData(userData: CredencialsData | null): void {
    this.userData = userData;
  }

  getUserData(): CredencialsData | null {
    return this.userData;
  }
}
