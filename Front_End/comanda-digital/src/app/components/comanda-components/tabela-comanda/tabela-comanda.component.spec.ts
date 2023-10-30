import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabelaComandaComponent } from './tabela-comanda.component';

describe('TabelaComandaComponent', () => {
  let component: TabelaComandaComponent;
  let fixture: ComponentFixture<TabelaComandaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TabelaComandaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabelaComandaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
