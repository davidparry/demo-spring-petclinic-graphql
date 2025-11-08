-- Indexes for statistical query performance
-- These indexes optimize the pet statistics queries for sub-500ms response times

-- Index for pet count by type query
-- Improves GROUP BY performance on pets.type_id
CREATE INDEX IF NOT EXISTS idx_pets_type_id ON pets(type_id);

-- Composite index for top services query
-- Improves JOIN and GROUP BY performance on visits
CREATE INDEX IF NOT EXISTS idx_visits_pet_vet ON visits(pet_id, vet_id);

-- Composite index for vet specialties lookup
-- Improves JOIN performance when linking vets to specialties
CREATE INDEX IF NOT EXISTS idx_vet_specialties_composite ON vet_specialties(vet_id, specialty_id);
