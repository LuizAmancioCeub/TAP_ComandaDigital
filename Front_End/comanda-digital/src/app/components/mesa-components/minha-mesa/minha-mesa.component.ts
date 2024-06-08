import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import jsQR from 'jsqr';
import { CredencialsData } from 'src/app/Models/CredencialsData';
import { MesaData } from 'src/app/Models/mesaData';
import { AxiosService } from 'src/app/services/axios.service';
import { MesaService } from 'src/app/services/mesa.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-minha-mesa',
  templateUrl: './minha-mesa.component.html',
  styleUrls: ['./minha-mesa.component.css']
})
export class MinhaMesaComponent {
  constructor(private userService:UserService, private axiosService:AxiosService, public mesaService:MesaService, private modalService:NgbModal){}
  @ViewChild('videoElement') videoElement!: ElementRef;
  userData:CredencialsData|null = null;
 
  mesaNum:number=0
  mesaGarcom:string=''
  cpf:string = ''

  mostrarErro: boolean = false;
  erro:string = "";
  alert:string = "";
  icon:string = "";

  ngOnInit(): void {
    this.initUserData();
  }
  async initUserData(): Promise<void> {
    this.userData = await this.userService.getUserData();
    if (this.userData !== null) {
      this.recuperarUser();
    } else {
      console.error("Erro ao recuperar dados do usuário");
    }
  }

  close() {
    this.closeCamera();
    this.modalService.dismissAll();
    this.mostrarErro = false;
    this.mesaService.camera = false;
    this.cameraLigada = false;
    this.mesaService.alterarMesa = false;
  }
  recuperarUser(){
    if(this.userData != null){
      this.mesaNum = this.userData.mesa.numero
      this.mesaGarcom = this.userData.mesa.garcom
      this.cpf = this.userData.cpf
    }
  }

  // funções relacionadas a alteração de mesa por qrCode

  // abrir camera de dispositov
  openCamera(): void {
    this.mesaService.camera = true;
    this.cameraLigada = true;
    const constraints: MediaStreamConstraints = {
      video: {
        facingMode: "environment" // Use "user" para a câmera frontal
      }
    };
    navigator.mediaDevices.getUserMedia({ video: true })
      .then(stream => {
        this.videoElement.nativeElement.srcObject = stream;
        this.capturarQRCode();
      })
      .catch(error => {
        console.error('Erro ao acessar a câmera: ', error);
      });
  }

  closeCamera(): void {
    if (this.videoElement && this.videoElement.nativeElement.srcObject) {
      const stream = this.videoElement.nativeElement.srcObject as MediaStream;
      const tracks = stream.getTracks();
      tracks.forEach(track => track.stop());
      this.videoElement.nativeElement.srcObject = null;
      this.mesaService.camera = false;
      this.cameraLigada = false;
    }
  }

  cameraLigada: boolean = false;
  novaMesa:string | null = null;
  decodeQRCode(imageData: ImageData): string | null {
    // Decodifica o código QR a partir dos dados da imagem
    const code = jsQR(imageData.data, imageData.width, imageData.height);
  
    // Retorna o conteúdo decifrado do código QR, ou null se nenhum código QR for encontrado
    return code ? code.data : null;
  }

  // função que fica verificando a existência do qrCode na camera
  capturarQRCode(): void {
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d', {willReadFrequently:true});
    
    const verificarQRCode = () => {
      if (context && this.videoElement?.nativeElement && this.videoElement.nativeElement.videoWidth > 0) {
        const video = this.videoElement.nativeElement;
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
  
        const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
        const qrCodeContent = this.decodeQRCode(imageData);
        
        if (qrCodeContent) {
          this.novaMesa = qrCodeContent;
          this.closeCamera();
          this.cameraLigada = false;
          this.verificarNovaMesa(qrCodeContent)
        }
      }
  
      // Continua verificando enquanto a câmera estiver ligada
      if (this.cameraLigada) {
        requestAnimationFrame(verificarQRCode);
      }
    };
    verificarQRCode();

    setTimeout(() => {
      this.closeCamera();
    }, 20000);
  }

  mesaData:MesaData[] = []
  novaMesaNum:number = 0;
  novaMesaGarcom:string='';
  // após verificar existencia e decodificação do qrCode, é necessário verificar se o qrCode bate com os qrCodes das mesas existentes
  verificarNovaMesa(novoQrCode: string): void {
    this.mesaService.recuperarMesas().then((response) => {
      const mesas = response.data;
      this.mesaData = mesas;
      
      if (novoQrCode && (novoQrCode.substring(0, 7).includes('http://') || novoQrCode.substring(0, 7).includes('https://'))) {
        novoQrCode = novoQrCode.substring(7)
      }
      for(const mesa of this.mesaData){
        if(novoQrCode == mesa.qr_code){
          if(mesa.id == this.mesaNum){
            this.mostrarMsg("você já está vinculado a essa mesa !",2);
            break;
          }
          if(mesa.status.id == 14){
            this.mostrarMsg("Mesa indisponível para vinculação",2);
            break;
          }
          this.mostrarMsg("Confirme a alteração da mesa: "+this.mesaNum+" ➡️ "+mesa.id ,1);
          this.novaMesaNum = mesa.id
          this.novaMesaGarcom = mesa.garcom.nome
          this.mesaService.alterarMesa = true;
          break; 
        }
      }
      if(this.mostrarErro == false){
        this.mostrarMsg("QrCode inválido",2);
      }
    }).catch((error) => {
      const responseData = error.response.data;
      if(responseData.fields){
        const errorFields = responseData.fields;
        const fieldName = Object.keys(errorFields)[0];
        const fieldError = errorFields[fieldName];
        this.mostrarMsg(fieldError,2);
      }else{
        const errorDetail = responseData.detail;
        this.mostrarMsg(errorDetail,2);
      }
    }); 
  }

  fecharMsg(){
    this.mostrarErro = false;
  }

  mostrarMsg(mensagem:string, tipo:number):void{
    this.mostrarErro = false;
    if(tipo == 1){
      this.alert = "success"
      this.erro = mensagem
      this.icon = "bi bi-bookmark-checkl";
      this.mostrarErro = true;
    }else if( tipo == 2){
      this.alert = "warning"
      this.erro = mensagem
      this.icon = "bi bi-exclamation-triangle-fill";
      this.mostrarErro = true;
      setTimeout(() => {
        this.mostrarErro = false;
      }, 5000);
    }
      
  }

  salvarMesa(cpf:string, novaMesa:number, mesaAtual:number){
    this.mesaService.alterarMesaCliente(cpf, novaMesa, mesaAtual).then((response) => {
      this.cameraLigada = false;
      this.mesaService.camera = false;
      this.mesaService.alterarMesa = false;
      this.mesaService.recuperarById(novaMesa).then((response) => {
        this.mesaNum = response.data.id
        this.mesaGarcom = response.data.garcom.nome
      }).catch((error) => {
        this.mostrarMsg("Erro ao resgatar nova mesa",2)
      })
      this.mostrarMsg(response.data,1)
    })
    .catch((error) => {
      const responseData = error.response.data;
      if(responseData.fields){
        const errorFields = responseData.fields;
        const fieldName = Object.keys(errorFields)[0];
        const fieldError = errorFields[fieldName];
        this.mostrarMsg(fieldError,2);
      }else{
        const errorDetail = responseData.detail;
        this.mostrarMsg(errorDetail,2);
      }
    })
  }
}
