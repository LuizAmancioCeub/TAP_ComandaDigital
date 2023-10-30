import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabelaPreparoComponent } from './tabela-preparo.component';

describe('TabelaPreparoComponent', () => {
  let component: TabelaPreparoComponent;
  let fixture: ComponentFixture<TabelaPreparoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TabelaPreparoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TabelaPreparoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
