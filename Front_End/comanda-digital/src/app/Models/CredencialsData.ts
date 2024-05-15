export type CredencialsData = {
    nome:string
    login:string
    cpf:string
    telefone:string
    email:string
    perfil:{
        id:number
        perfil:string
    }
    mesa: {
        numero:number
        garcom:string
    }
}