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