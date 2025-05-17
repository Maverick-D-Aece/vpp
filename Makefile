infra:
	docker-compose -f docker-compose.yaml down && docker-compose -f docker-compose.yaml up -d

cleanup:
	docker-compose -f docker-compose.yaml down