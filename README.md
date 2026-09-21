# InovaGAB - Aplicativo Mobile (Android)

Repositório destinado ao código-fonte do aplicativo nativo Android **InovaGAB**, desenvolvido pelo Challenge FIAP em parceria com o **Grupo Águia Branca**

O InovaGAB é uma solução mobile intuitiva que conecta colaboradores operacionais, gestores e lideranças, permitindo o registro de ideias, acompanhamento de projetos e visualização de dashboards estratégicos diretamente do smartphone.

---

##  Tecnologias e Arquitetura

O aplicativo foi construído utilizando práticas de desenvolvimento Android nativo:

*   **Linguagem:** Kotlin
*   **Interface de Usuário (UI):** Jetpack Compose (UI Declarativa)
*   **Arquitetura:** Clean Architecture (Separação em camadas: UI, Domain, Data) e padrão MVVM (Model-View-ViewModel)
*   **Comunicação de Rede:** Retrofit2 + OkHttp3 (com `HttpLoggingInterceptor` e tratamento robusto via `safeApiCall` e `ApiResult<T>`)
*   **Parsing de Dados:** Moshi nativo para Kotlin
*   **Assincronismo:** Kotlin Coroutines e Flows
*   **Segurança Local:** `SessionManager` para armazenamento e injeção de tokens JWT (Bearer) nas requisições.

---

##  Perfis e Funcionalidades (App)

A interface se adapta dinamicamente com base no nível de acesso do usuário autenticado

1.  **Operador:** Interface simplificada para consulta de diretrizes e formulário ágil para cadastro de problemas e ideias do dia a dia.
2.  **Gestor:** Telas de curadoria para aprovação de ideias e formulários avançados para a conversão dessas ideias em Projetos reais, com atualização de progresso.
3.  **Líder:** Painel de visualização de portfólio e acesso ao **Dashboard de Resultados**, que consome dados processados pela Inteligência Artificial no backend.

---

Como Executar o App Localmente

Siga o passo a passo para rodar o aplicativo no emulador ou dispositivo físico.

### Pré-requisitos
*   [Android Studio](https://developer.android.com/studio) (versão Iguana ou superior recomendada).
*   Backend do InovaGAB (Spring Boot) rodando localmente.

### 1. Clonar e Abrir
```bash
git clone [https://github.com/seu-usuario/inovagab-android.git](https://github.com/seu-usuario/inovagab-android.git)
```
Abra a pasta `inovagab-android` no Android Studio e aguarde a sincronização do Gradle.

### 2. Configuração de Rede (Emulador)
Por padrão, o aplicativo está configurado no `RetrofitClient` para se comunicar com o backend local através do IP do emulador do Android Studio (`10.0.2.2`). 
*Nota: O arquivo `AndroidManifest.xml` já possui a tag `android:usesCleartextTraffic="true"` para permitir tráfego HTTP local em ambiente de desenvolvimento.*

Se for testar em um **dispositivo físico**, atualize a `BASE_URL` no arquivo `RetrofitClient.kt` para o IP da sua máquina na rede Wi-Fi local (ex: `http://192.168.1.15:8080/`).

### 3. Compilar e Gerar APK
Para rodar diretamente: Pressione `Shift + F10` (Run 'app') com um emulador selecionado.

Para gerar o arquivo **APK final**:
1. No menu superior, vá em **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**.
2. Aguarde o processo finalizar e clique em "locate" no pop-up do canto inferior direito. O arquivo `app-debug.apk` estará na pasta `app/build/outputs/apk/debug/`.

---

## Desenvolvido por:
*   **Higor Proença** - Análise e Desenvolvimento de Sistemas (FIAP)
