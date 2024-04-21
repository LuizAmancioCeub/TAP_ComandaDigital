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
    valorTotal:number
    status:string
    cliente:{
        nome:string
        telefone:string
        login:string
    }
    mesa:number
    pedidos:{
        idItem:number
        nomeItem:string
        precoItem:number
        quantidade:number
        valor:number
        horario_dataPedido: string
        horarioPedido:Date
        status:{
            id:number
            status:string
        }
    }[]
}