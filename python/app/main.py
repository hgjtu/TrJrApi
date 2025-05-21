from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.v1.endpoints import auth, journal
from app.core.config import settings

app = FastAPI(
    title="Travel Journal API",
    description="API for managing travel journals",
    version="1.0.0",
)

# CORS middleware configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(auth.router, prefix="/api/v1/auth", tags=["auth"])
app.include_router(journal.router, prefix="/api/v1/journals", tags=["journals"])

@app.get("/")
async def root():
    return {"message": "Welcome to Travel Journal API"} 