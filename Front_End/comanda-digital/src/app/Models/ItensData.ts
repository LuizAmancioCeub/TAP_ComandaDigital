export type ItensData = {
    id:number
    nome:string
    descricao:string
    preco:number
    imagem:string
    categoria:{
        id:number
        categoria:string
    }
    status:{
        status:string
    }
}