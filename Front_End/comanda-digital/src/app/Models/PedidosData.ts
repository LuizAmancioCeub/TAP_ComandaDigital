export type PedidosData = {
    id:number
    valor:number
    quantidade:number
    observacao:string
    horario_dataPedido: string
    horarioPedido: Date | undefined;
    horario_dataEntrega: string
    horarioEntrega: Date | undefined;
    item:{
        id:number
        nome:string
        descricao:string
        preco:number
        imagem:string
        categoria:{
            id:number
            categoria:string
        }
    }
    status:{
        id:number
        status:string
    },
    comanda:{
        id:number
        status:{
            status:string
        }
        cliente:{
            login:string
            nome:string,
            mesa:{
                id:number
            }
        }
    }
    
}