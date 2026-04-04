package br.com.maikonspo.consumo.core.exception

class WaterExpenseAlreadyExistsException :
    RuntimeException("Já existe um gasto de água cadastrado para este usuário neste mês/ano")