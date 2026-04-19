# Water Consumption Cross-Platform

Aplicativo novo em React Native com Expo para demonstracao em Android e iPhone.

## O que ele faz

- login com JWT via `POST /auth/login`
- listagem de contas via `GET /api/water-expenses`
- cadastro de conta via `POST /api/water-expenses`
- logout

## Requisito mais importante

O app depende da API backend deste projeto estar rodando.

No iPhone, o endereco da API nao pode ser `localhost` nem `10.0.2.2`.
Voce precisa usar o IP do seu computador na rede local.

Exemplo:

```json
"apiUrl": "http://192.168.0.10:8080"
```

Esse valor fica em:

- `app.json`

Voce tambem pode sobrescrever com:

```bash
EXPO_PUBLIC_API_URL=http://192.168.0.10:8080
```

## Como rodar

1. Instale Node.js 20.19+ na sua maquina.
2. Entre na pasta:

```bash
cd "D:\Projetos Android\consumo-agua-kotlin\mobile_react_native"
```

3. Instale as dependencias:

```bash
npm install
```

Se voce tinha instalado dependencias antes com Expo SDK 53, limpe antes de reinstalar:

```bash
rmdir /s /q node_modules
del package-lock.json
npm install
```

4. Inicie o projeto:

```bash
npm run start
```

5. No iPhone:
- instale o app `Expo Go`
- escaneie o QR Code exibido no terminal ou no navegador

## Como apresentar no iPhone

1. Ligue a API no seu computador.
2. Descubra o IP do PC na rede local.
3. Ajuste `app.json` para esse IP.
4. Deixe iPhone e computador na mesma rede Wi-Fi.
5. Abra com o `Expo Go`.

## Observacao

Como este ambiente nao tinha um gerenciador `npm` disponivel no PATH, a estrutura do projeto foi criada manualmente e precisa de `npm install` na sua maquina antes da primeira execucao.
