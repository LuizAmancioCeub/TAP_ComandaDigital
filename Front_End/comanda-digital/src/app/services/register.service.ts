import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

function validatePhone(control: FormControl): { [key: string]: any } | null {
  const phoneRegex = /^[0-9]{11}$/; // Verifica se o número tem 11 dígitos
  const isValid = phoneRegex.test(control.value);
  return isValid ? null : { invalidPhone: { value: control.value } };
}

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  phoneForm: FormGroup;

  constructor(private axiosService:AxiosService) {
    this.phoneForm = new FormGroup({
      phone: new FormControl('', [Validators.required, validatePhone])
    });
   }


   register(cpf:string, nome:string,telefone:string, senha:string):Promise<any>{
    return this.axiosService.request(
       "POST",
       "/login/registrar",
       {
         cpf: cpf,
         nome: nome,
         telefone: telefone,
         senha: senha
       }
     );
   }

   conferirCampos(cpf: string, nome: string, telefone: string, senha: string, senhaB: string): string {
    if (cpf === "" || nome === "" || telefone === "" || senha === "" || senhaB === "") {
      return "campos";
    } else if (!this.validarNome(nome)) {
      return "nome";
    } else if (!this.validarCPF(cpf)) {
      return "cpf";
    }else if (!this.validarTelefone(telefone)) {
      return "Telefone";
    } else if (senha !== senhaB || senha.length < 3 || senha.length > 12) {
      return "senha";
    } else {
      return "ok";
    }
  }

  validarTelefone(telefone: string): boolean {
    if (telefone.length !== 11) {
      return false;
    } else {
      const phoneRegex = /^[0-9]{11}$/;
      return phoneRegex.test(telefone);
    }
  }
  
  validarCPF(cpf: string): boolean {
    if (cpf.length !== 11) {
      return false;
    } else {
      const cpfRegex = /^[0-9]{11}$/;
      return cpfRegex.test(cpf);
    }
  }
  
  validarNome(nome: string): boolean {
    const nameRegex = /^[a-zA-ZÀ-ÿ\s]*$/;
    return nameRegex.test(nome);
  }
}
