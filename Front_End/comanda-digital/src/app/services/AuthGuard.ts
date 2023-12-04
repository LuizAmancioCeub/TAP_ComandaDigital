import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LoginService } from './login.service';
import { LoaderService } from './loader.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: LoginService, private router: Router, private loaderService: LoaderService) { }

  canActivate(): boolean {
    //this.loaderService.showLoader();
    if (!this.authService.isAuthenticated()) {
      //this.loaderService.hideLoader();
      this.router.navigate(['/']);
      return false;
    }
    //this.loaderService.hideLoader();
    return true;
  }
}
