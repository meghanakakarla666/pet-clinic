#!/bin/bash

echo "╔══════════════════════════════════════════════════════════════════════════════╗"
echo "║                        🐳 DOCKER CONTAINERS VIEW                             ║"
echo "╚══════════════════════════════════════════════════════════════════════════════╝"
echo ""

# Summary Stats
echo "📊 OVERALL STATS:"
echo "════════════════════════════════════════════════════════════════════════════════"
RUNNING=$(docker ps -q | wc -l | tr -d ' ')
TOTAL=$(docker ps -aq | wc -l | tr -d ' ')
IMAGES=$(docker images -q | wc -l | tr -d ' ')

echo "   Running Containers: $RUNNING"
echo "   Total Containers:   $TOTAL"
echo "   Total Images:       $IMAGES"
echo ""

# Running Containers
echo "🟢 RUNNING CONTAINERS:"
echo "════════════════════════════════════════════════════════════════════════════════"
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}" | grep -v "^NAMES" | while read line; do
    echo "   $line"
done
echo ""

# Resource Usage
echo "💻 RESOURCE USAGE:"
echo "════════════════════════════════════════════════════════════════════════════════"
docker stats --no-stream --format "   {{.Name}}\t CPU: {{.CPUPerc}}\t MEM: {{.MemUsage}}\t NET: {{.NetIO}}"
echo ""

# All Containers
echo "📦 ALL CONTAINERS (including stopped):"
echo "════════════════════════════════════════════════════════════════════════════════"
docker ps -a --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}" | tail -n +2 | while read line; do
    echo "   $line"
done
echo ""

# Images
echo "🖼️  DOCKER IMAGES:"
echo "════════════════════════════════════════════════════════════════════════════════"
docker images --format "   {{.Repository}}:{{.Tag}}\t{{.Size}}\t{{.CreatedSince}}"
echo ""

echo "╔══════════════════════════════════════════════════════════════════════════════╗"
echo "║  💡 Tip: Run 'docker logs <container-name>' to view container logs           ║"
echo "║  💡 Tip: Run 'docker exec -it <container-name> bash' to enter container      ║"
echo "╚══════════════════════════════════════════════════════════════════════════════╝"
