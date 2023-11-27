package com.comandadigital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comandadigital.models.PedidoItemModel;
import com.comandadigital.models.PedidoItemPK;

public interface PedidoItemRepository extends JpaRepository<PedidoItemModel, PedidoItemPK> {

}
