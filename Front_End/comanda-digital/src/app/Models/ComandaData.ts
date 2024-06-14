import { DatePipe } from "@angular/common"

export type ComandaData = {
    id:number
    valorTotal:number
    status:{
        id:number
        status:string
    }
    cliente:{
        login:string
        nome:string
    }
    mesa:{
        id:number
    }
}

export type ComandaClienteData = {
    id:number
    valorTotal:number
    status:string
    cliente:{
        nome:string
        telefone:string
        login:string
    }
    mesa:number
    garcom:string
    pedidos:{
        idPedido:number
        idItem:number
        nomeItem:string
        precoItem:number
        quantidade:number
        valor:number
        observacao:string
        horarioPedido:string
        horarioEntrega:string
        imagem:string
        status:{
            id:number
            status:string
        }
    }[]
    dtAbertura:string
}