# IdiomasBR - Plataforma Inteligente de Ensino de Idiomas

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green)
![Java](https://img.shields.io/badge/Java-21-blue)
![License](https://img.shields.io/badge/license-MIT-green)

Plataforma de ensino de idiomas que utiliza IA para personalizar o aprendizado.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.3.5
- PostgreSQL
- Redis
- Docker
- Kubernetes
- AWS Cloud
- React (Frontend)

## 📋 Requisitos

- JDK 21
- Maven
- Docker
- Kubernetes CLI
- AWS CLI

## 🛠️ Instalação

```bash
# Clonar repositório
git clone https://github.com/IdiomasBR/platform.git

# Instalar dependências
mvn install

# Configurar variáveis de ambiente
cp .env.example .env

# Iniciar banco de dados
docker-compose up -d postgres redis

# Executar aplicação
mvn spring-boot:run
```

## 🏗️ Arquitetura

```
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── k8s/
│   ├── deployment.yaml
│   └── service.yaml
├── terraform/
│   ├── main.tf
│   └── variables.tf
└── docker-compose.yml
```

## 🔐 Segurança

- OAuth2 para autenticação
- Spring Security
- AWS WAF
- Shield Advanced
- KMS para criptografia

## 📊 Monitoramento

- Prometheus
- Grafana
- ELK Stack
- CloudWatch

## 🔄 CI/CD

- GitHub Actions
- Docker Registry
- Kubernetes Deployments
- AWS EKS

## 🔧 Configuração

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/idiomasbr
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
```

## 📚 API Endpoints

```
GET    /api/courses          # Lista cursos
POST   /api/users           # Cria usuário
GET    /api/lessons/{id}    # Obtém lição
POST   /api/auth/login      # Login
```

## 🎯 Features

- [x] Autenticação OAuth2
- [x] Integração com IA
- [x] Sistema de níveis
- [x] Avaliação automática
- [ ] Chat em tempo real
- [ ] Reconhecimento de voz

## 🤝 Contributing

1. Fork
2. Crie branch (`git checkout -b feature/nova-feature`)
3. Commit (`git commit -m 'Add: nova feature'`)
4. Push (`git push origin feature/nova-feature`)
5. Pull Request

## 📄 License

MIT License - veja [LICENSE](LICENSE) para mais detalhes.

## 🌐 Links

- [Documentação](https://docs.idiomasbr.com)
- [API Reference](https://api.idiomasbr.com/docs)
- [Status Page](https://status.idiomasbr.com)

## 📞 Suporte

- Email: support@idiomasbr.com
- Discord: [IdiomasBR](https://discord.gg/idiomasbr)
- Issues: [GitHub Issues](https://github.com/IdiomasBR/platform/issues)
