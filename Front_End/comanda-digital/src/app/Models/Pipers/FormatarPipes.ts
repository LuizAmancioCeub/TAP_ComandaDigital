import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatarCpf'
})
export class FormatarCpfPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';
    value = value.replace(/\D/g, '');
    return value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }
}

@Pipe({
  name: 'formatarTelefone'
})
export class FormatarTelefonePipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';
    value = value.replace(/\D/g, '');
    return value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  }
}