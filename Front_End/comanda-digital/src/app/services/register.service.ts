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

  conferirCampos(cpf: string,nome:string,telefone:string, senha: string, senhaB:string): string{
    if(cpf == "" || nome == "" || telefone == null || senha == null || senhaB == null){
      return "campos"
    }
    else if(!this.validarCPF){
      return "cpf"
    }
    else if(!this.validarNome){
      return "nome"
    }
    else if(!this.validarTelefone){
      return "Telefone"
    }
    else if(senha !== senhaB){
      return "senha"
    }
    else{
      return "ok"
    }
  }

  validarTelefone(telefone:string):boolean{
    if (telefone.length !== 11) {
      return false
    } 
    else {
      const phoneRegex = /^[0-9]{11}$/;
      const isValid = phoneRegex.test(telefone);
      if (!isValid) {
       return false
      }
      return true;
    }
  }

  validarCPF(cpf:string):string {
    if (cpf.length !== 11) {
      return "false"
    } else {
      const cpfRegex = /^[0-9]{11}$/;
      const isValid = cpfRegex.test(cpf);
      if (!isValid) {
        return "false"
      }
      return "true";
    }
  }  

  validarNome(nome:string):boolean {
    if (!nome.match(/^[a-zA-ZÀ-ÿ\s]*$/)) {
     return false;
    }
    return true;
  }
}
