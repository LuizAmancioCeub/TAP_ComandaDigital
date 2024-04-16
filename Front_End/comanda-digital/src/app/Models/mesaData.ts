export type MesaData = {
    id:number
    qr_code:string
    garcom:{
        nome:string
    }
    status:{
        id:number
        status:string
    }
}

export type ClientesMesaData = {
    nome:string
    login:string
    telefone:string
}