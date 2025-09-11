# 旅行超级智能体 (Travel Super Agent)

基于Spring AI构建的智能旅行助手，为用户提供全方位的旅行规划与推荐服务。

## 项目概述

旅行超级智能体利用人工智能技术，整合多源旅行数据，为用户打造个性化、智能化的旅行体验。无论是商务出行还是休闲旅游，都能通过自然语言交互获得精准的旅行建议和完整的行程规划。


## 核心功能

- ✅ 智能行程规划：根据时间、预算、兴趣自动生成最优旅行计划
- ✅ 个性化推荐：基于用户偏好推荐景点、餐厅和活动
- ✅ 实时信息查询：获取目的地天气、交通、安全等实时信息
- ✅ 酒店与交通预订建议：根据行程推荐最合适的住宿和交通方式
- ✅ 自然语言交互：通过对话式界面轻松获取旅行建议

## 技术栈

- 核心框架：Spring Boot 3.4.9
- AI能力：Spring AI
- 语言模型：支持多种主流LLM集成
- 数据存储：待补充>>>>
- API集成：待补充>>>>

## 快速开始

### 前提条件

- JDK 17+
- Maven 3.6+
- 有效的API密钥（根据使用的AI模型）

### 安装步骤

1. 克隆仓库git clone https://github.com/yourusername/travel-super-agent.git
   cd travel-super-agent
2. 配置API密钥
   在`application.properties`中配置你的AI模型API密钥：spring.ai.openai.api-key=your_api_key_here
3. 构建并运行mvn clean package
   java -jar target/travel-super-agent-0.0.1-SNAPSHOT.jar
4. 访问应用
   打开浏览器访问 http://localhost:8080

## 使用示例
用户：我计划下月初去巴黎旅行5天，预算1万元，喜欢艺术和美食
智能体：为您规划了巴黎5日艺术美食之旅...
## 项目结构
待完善>>>>
## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开Pull Request

## 许可证

本项目采用MIT许可证 - 详见LICENSE文件

## 联系方式

项目维护者: [zhang](2960885715@qq.com)
