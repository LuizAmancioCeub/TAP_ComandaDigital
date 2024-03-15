import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})

// verificação de perfil do usuário para cada página
export class RoleGuard implements CanActivate {
  constructor(private authService: LoginService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const allowedRoles = next.data['allowedRoles']; // Lista de perfis permitidos
    return this.authService.getUserRole().then(perfil => {
        if (!allowedRoles.includes(perfil)) {
            this.router.navigate(['/']); // Redirecionar para uma rota de acesso não autorizado
            return false;
        }
      return true;
    }).catch(error => {
      console.error('Erro ao verificar o perfil do usuário:', error);
      return false;
    });
  }
}