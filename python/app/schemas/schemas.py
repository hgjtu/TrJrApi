from pydantic import BaseModel, EmailStr
from datetime import datetime
from typing import Optional, List

class UserBase(BaseModel):
    email: EmailStr
    full_name: str

class UserCreate(UserBase):
    password: str

class User(UserBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True

class JournalBase(BaseModel):
    title: str
    description: Optional[str] = None
    location: Optional[str] = None
    start_date: Optional[datetime] = None
    end_date: Optional[datetime] = None

class JournalCreate(JournalBase):
    pass

class Journal(JournalBase):
    id: int
    owner_id: int
    created_at: datetime

    class Config:
        from_attributes = True

class JournalEntryBase(BaseModel):
    title: str
    content: str
    date: datetime
    location: Optional[str] = None

class JournalEntryCreate(JournalEntryBase):
    journal_id: int

class JournalEntry(JournalEntryBase):
    id: int
    created_at: datetime

    class Config:
        from_attributes = True

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    email: Optional[str] = None 