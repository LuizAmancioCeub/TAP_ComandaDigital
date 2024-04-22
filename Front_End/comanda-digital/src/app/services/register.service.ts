import { Injectable } from '@angular/core';
import { AxiosService } from './axios.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
//import * as bcrypt from 'bcryptjs'; // Importa a biblioteca bcrypt.js

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

  /* Função para criptografar a senha
  encryptPassword(password: string): string {
    const saltRounds = 10; // Número de rounds de hashing
    const salt = bcrypt.genSaltSync(saltRounds);
    return bcrypt.hashSync(password, salt);
  }
*/

   register(cpf:string, nome:string,telefone:string, senha:string):Promise<any>{
   // const encryptedPassword = this.encryptPassword(senha);
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
      return "telefone";
    }else if (senha.length < 6 || senha.length > 12) {
      return "TamanhoSenha"; 
    }else if (senha !== senhaB) {
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
