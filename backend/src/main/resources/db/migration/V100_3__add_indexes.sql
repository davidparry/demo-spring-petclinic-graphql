-- Migration to add indexes for pet statistics queries
-- These indexes optimize the performance of statistical aggregation queries

-- Index for pet type lookups (used in pet count by type query)
CREATE INDEX IF NOT EXISTS idx_pets_type_id ON pets(type_id);

-- Composite index for visit-pet-vet relationships (used in top services query)
CREATE INDEX IF NOT EXISTS idx_visits_pet_vet ON visits(pet_id, vet_id);

-- Composite index for vet-specialty relationships (used in top services query)
CREATE INDEX IF NOT EXISTS idx_vet_specialties_composite ON vet_specialties(vet_id, specialty_id);
