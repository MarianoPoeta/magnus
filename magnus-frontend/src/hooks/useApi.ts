import { useCallback, useEffect, useState } from 'react';
import { useStore } from '../store';
import { apiServices, ApiError, authService } from '../services/api';

// Custom hook for API integration with store
export const useApi = () => {
  const store = useStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Error handler
  const handleError = useCallback((error: ApiError) => {
    setError(error.message);
    console.error('API Error:', error);
    
    // Handle specific error cases
    if (error.status === 401) {
      // Unauthorized - redirect to login
      store.setCurrentUser(null);
      localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
      window.location.href = '/login';
    } else if (error.status === 403) {
      // Forbidden - show access denied message
      setError('Access denied. You do not have permission to perform this action.');
    }
  }, [store]);

  // Clear error
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  // Authentication
  const login = useCallback(async (credentials: { username: string; password: string; rememberMe?: boolean }) => {
    setIsLoading(true);
    clearError();
    
    try {
      const res = await apiServices.auth.login(credentials);
      if (res.id_token) {
        // After login, fetch current user
        const user = await apiServices.auth.getCurrentUser();
        store.setCurrentUser({
          id: user?.id,
          name: `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.login,
          email: user?.email,
          role: (user?.authorities?.includes('ROLE_ADMIN') ? 'admin' : 'sales') as any,
        } as any);
      }
      return res;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const logout = useCallback(async () => {
    try {
      await apiServices.auth.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear local data regardless of API response
      localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
      store.setCurrentUser(null);
    }
  }, [store]);

  // Budget Management
  const loadBudgets = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.getBudgets(params);
      store.setBudgets(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createBudget = useCallback(async (budgetData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.createBudget(budgetData);
      store.addBudget(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateBudget = useCallback(async (id: string, budgetData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.updateBudget({ ...budgetData, id });
      store.updateBudget(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteBudget = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.budgets.deleteBudget(id);
      store.deleteBudget(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Activity Management
  const loadActivities = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.getActivities(params);
      store.setActivities(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createActivity = useCallback(async (activityData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.createActivity(activityData);
      store.addActivity(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateActivity = useCallback(async (id: string, activityData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.updateActivity({ ...activityData, id });
      store.updateActivity(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteActivity = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.activities.deleteActivity(id);
      store.deleteActivity(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Accommodation Management
  const loadAccommodations = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.accommodations.getAccommodations(params);
      const mapped = (response.data || []).map((a: any) => ({
        id: String(a.id),
        name: a.name,
        description: a.description,
        roomType: String(a.type || 'DOUBLE').toLowerCase(),
        pricePerNight: Number(a.pricePerNight ?? 0),
        costPerNight: Number(a.costPerNight ?? 0),
        maxCapacity: Number(a.maxOccupancy ?? 0),
        address: a.address,
        amenities: a.amenities ? a.amenities.split(',').map((s: string) => s.trim()).filter(Boolean) : [],
        isActive: !!a.isActive,
        createdAt: a.createdAt,
        updatedAt: a.updatedAt,
      }));
      store.setAccommodations(mapped as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createAccommodation = useCallback(async (accommodationData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const payload = {
        name: accommodationData.name,
        description: accommodationData.description,
        type: String(accommodationData.roomType || 'double').toUpperCase(),
        pricePerNight: Number(accommodationData.pricePerNight ?? 0),
        costPerNight: Number(accommodationData.costPerNight ?? 0),
        maxOccupancy: Number(accommodationData.maxCapacity ?? 1),
        address: accommodationData.address,
        amenities: (accommodationData.amenities || []).join(', '),
        checkInTime: accommodationData.checkInTime,
        checkOutTime: accommodationData.checkOutTime,
        rating: accommodationData.rating,
        contactInfo: accommodationData.contactInfo,
        isActive: accommodationData.isActive ?? true,
        isTemplate: false,
        createdAt: accommodationData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.accommodations.createAccommodation(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        roomType: String((response as any).type || 'DOUBLE').toLowerCase(),
        pricePerNight: Number((response as any).pricePerNight ?? 0),
        costPerNight: Number((response as any).costPerNight ?? 0),
        maxCapacity: Number((response as any).maxOccupancy ?? 0),
        address: (response as any).address,
        amenities: ((response as any).amenities || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.addAccommodation(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateAccommodation = useCallback(async (id: string, accommodationData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const payload = {
        id: Number(id),
        name: accommodationData.name,
        description: accommodationData.description,
        type: String(accommodationData.roomType || 'double').toUpperCase(),
        pricePerNight: Number(accommodationData.pricePerNight ?? 0),
        costPerNight: Number(accommodationData.costPerNight ?? 0),
        maxOccupancy: Number(accommodationData.maxCapacity ?? 1),
        address: accommodationData.address,
        amenities: (accommodationData.amenities || []).join(', '),
        checkInTime: accommodationData.checkInTime,
        checkOutTime: accommodationData.checkOutTime,
        rating: accommodationData.rating,
        contactInfo: accommodationData.contactInfo,
        isActive: accommodationData.isActive ?? true,
        isTemplate: false,
        createdAt: accommodationData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.accommodations.updateAccommodation(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        roomType: String((response as any).type || 'DOUBLE').toLowerCase(),
        pricePerNight: Number((response as any).pricePerNight ?? 0),
        costPerNight: Number((response as any).costPerNight ?? 0),
        maxCapacity: Number((response as any).maxOccupancy ?? 0),
        address: (response as any).address,
        amenities: ((response as any).amenities || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.updateAccommodation(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteAccommodation = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.accommodations.deleteAccommodation(id);
      store.deleteAccommodation(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Menu Management
  const loadMenus = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.menus.getMenus(params);
      const mapped = (response.data || []).map((m: any) => ({
        id: String(m.id),
        name: m.name,
        description: m.description,
        type: String(m.type || 'DINNER').toLowerCase(),
        pricePerPerson: Number(m.pricePerPerson ?? 0),
        costPerPerson: Number(m.costPerPerson ?? 0),
        minPeople: Number(m.minPeople ?? 1),
        maxPeople: Number(m.maxPeople ?? 1),
        restaurant: m.restaurant,
        preparationTime: m.preparationTime ?? 0,
        isActive: !!m.isActive,
        selectedFoods: (m.includedFoodItems || []).map((fi: any) => String(fi.id)),
        createdAt: m.createdAt,
        updatedAt: m.updatedAt,
      }));
      store.setMenus(mapped as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createMenu = useCallback(async (menuData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const payload = {
        name: menuData.name,
        description: menuData.description,
        type: String(menuData.type || 'dinner').toUpperCase(),
        pricePerPerson: Number(menuData.pricePerPerson ?? 0),
        costPerPerson: Number(menuData.costPerPerson ?? 0),
        minPeople: Number(menuData.minPeople ?? 1),
        maxPeople: Number(menuData.maxPeople ?? 1),
        restaurant: menuData.restaurant || '',
        preparationTime: menuData.preparationTime ?? 0,
        isActive: menuData.isActive ?? true,
        isTemplate: false,
        version: menuData.version ?? 1,
        includedFoodItems: (menuData.selectedFoods || []).map((id: any) => ({ id: Number(id) })),
        createdAt: menuData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.menus.createMenu(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        type: String((response as any).type || 'DINNER').toLowerCase(),
        pricePerPerson: Number((response as any).pricePerPerson ?? 0),
        costPerPerson: Number((response as any).costPerPerson ?? 0),
        minPeople: Number((response as any).minPeople ?? 1),
        maxPeople: Number((response as any).maxPeople ?? 1),
        restaurant: (response as any).restaurant,
        preparationTime: (response as any).preparationTime ?? 0,
        isActive: !!(response as any).isActive,
        selectedFoods: ((response as any).includedFoodItems || []).map((fi: any) => String(fi.id)),
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.addMenu(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateMenu = useCallback(async (id: string, menuData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const payload = {
        id: Number(id),
        name: menuData.name,
        description: menuData.description,
        type: String(menuData.type || 'dinner').toUpperCase(),
        pricePerPerson: Number(menuData.pricePerPerson ?? 0),
        costPerPerson: Number(menuData.costPerPerson ?? 0),
        minPeople: Number(menuData.minPeople ?? 1),
        maxPeople: Number(menuData.maxPeople ?? 1),
        restaurant: menuData.restaurant || '',
        preparationTime: menuData.preparationTime ?? 0,
        isActive: menuData.isActive ?? true,
        isTemplate: false,
        version: menuData.version ?? 1,
        includedFoodItems: (menuData.selectedFoods || []).map((id: any) => ({ id: Number(id) })),
        createdAt: menuData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.menus.updateMenu(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        type: String((response as any).type || 'DINNER').toLowerCase(),
        pricePerPerson: Number((response as any).pricePerPerson ?? 0),
        costPerPerson: Number((response as any).costPerPerson ?? 0),
        minPeople: Number((response as any).minPeople ?? 1),
        maxPeople: Number((response as any).maxPeople ?? 1),
        restaurant: (response as any).restaurant,
        preparationTime: (response as any).preparationTime ?? 0,
        isActive: !!(response as any).isActive,
        selectedFoods: ((response as any).includedFoodItems || []).map((fi: any) => String(fi.id)),
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.updateMenu(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteMenu = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.menus.deleteMenu(id);
      store.deleteMenu(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Product Management
  const loadProducts = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.products.getProducts(params);
      // Map backend enums to frontend shapes
      const mapped = (response.data || []).map((p: any) => ({
        id: String(p.id),
        name: p.name,
        description: p.description,
        category: String(p.category || 'OTHER').toLowerCase(),
        unit: String(p.unit || 'UNITS').toLowerCase(),
        pricePerUnit: Number(p.pricePerUnit ?? 0),
        estimatedPrice: Number(p.pricePerUnit ?? 0),
        cost: Number(p.pricePerUnit ?? 0),
        supplier: p.supplier,
        notes: p.supplierContact,
        isActive: !!p.isActive,
        createdAt: p.createdAt,
        updatedAt: p.updatedAt,
      }));
      store.setProducts(mapped as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createProduct = useCallback(async (productData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      // Ensure backend enum and required fields
      const toBackend = async () => {
        const category = String(productData.category || 'other').toUpperCase();
        const unit = String(productData.unit || 'units').toUpperCase();
        // Resolve AppUser for createdBy if backend requires it
        let createdBy: any = undefined;
        try {
          const currentEmail = (store.currentUser as any)?.email;
          if (currentEmail) {
            const appUsersResp = await apiServices.appUsers.getAppUsers({ size: 1000 });
            const match = (appUsersResp.data || []).find((u: any) => u.email?.toLowerCase() === currentEmail.toLowerCase() || u.login?.toLowerCase() === (store.currentUser as any)?.name?.toLowerCase());
            if (match?.id != null) createdBy = { id: match.id };
          }
        } catch (err) {
          console.debug('createProduct: unable to resolve createdBy', err);
        }
        return {
          name: productData.name,
          description: productData.description,
          category,
          unit,
          pricePerUnit: Number(productData.pricePerUnit ?? productData.estimatedPrice ?? productData.cost ?? 0),
          minOrderQuantity: productData.minOrderQuantity,
          maxOrderQuantity: productData.maxOrderQuantity,
          supplier: productData.supplier,
          supplierContact: productData.notes,
          isActive: productData.isActive ?? true,
          createdAt: productData.createdAt || new Date().toISOString(),
          updatedAt: new Date().toISOString(),
          ...(createdBy ? { createdBy } : {}),
        };
      };
      const payload = await toBackend();
      const response = await apiServices.products.createProduct(payload);
      // Map back to frontend shape
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        category: String((response as any).category || 'other').toLowerCase(),
        unit: String((response as any).unit || 'units').toLowerCase(),
        pricePerUnit: Number((response as any).pricePerUnit ?? 0),
        estimatedPrice: Number((response as any).pricePerUnit ?? 0),
        cost: Number((response as any).pricePerUnit ?? 0),
        supplier: (response as any).supplier,
        notes: (response as any).supplierContact,
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.addProduct(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateProduct = useCallback(async (id: string, productData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const payload = {
        id: Number(id),
        name: productData.name,
        description: productData.description,
        category: String(productData.category || 'other').toUpperCase(),
        unit: String(productData.unit || 'units').toUpperCase(),
        pricePerUnit: Number(productData.pricePerUnit ?? productData.estimatedPrice ?? productData.cost ?? 0),
        minOrderQuantity: productData.minOrderQuantity,
        maxOrderQuantity: productData.maxOrderQuantity,
        supplier: productData.supplier,
        supplierContact: productData.notes,
        isActive: productData.isActive ?? true,
        createdAt: productData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.products.updateProduct(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        category: String((response as any).category || 'other').toLowerCase(),
        unit: String((response as any).unit || 'units').toLowerCase(),
        pricePerUnit: Number((response as any).pricePerUnit ?? 0),
        estimatedPrice: Number((response as any).pricePerUnit ?? 0),
        cost: Number((response as any).pricePerUnit ?? 0),
        supplier: (response as any).supplier,
        notes: (response as any).supplierContact,
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.updateProduct(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteProduct = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.products.deleteProduct(Number(id) as any);
      store.deleteProduct(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Food Management
  const loadFoods = useCallback(async () => {
    setIsLoading(true);
    clearError();
    try {
      const response = await apiServices.foodItems.getFoodItems();
      const mapped = (response as any[]).map((f: any) => {
        const cat = String(f.category || 'MAIN').toUpperCase();
        const category =
          cat === 'BEVERAGE' ? 'beverages' :
          cat === 'DESSERT' ? 'desserts' :
          cat === 'APPETIZER' ? 'appetizer' :
          cat === 'SPECIAL' ? 'special' :
          'main';
        return {
          id: String(f.id),
          name: f.name,
          description: f.description,
          category,
          basePrice: Number(f.basePrice ?? 0),
          pricePerUnit: Number(f.basePrice ?? 0),
          pricePerGuest: Number(f.basePrice ?? 0),
          unit: f.servingSize || 'unit',
          guestsPerUnit: Number(f.guestsPerUnit ?? 1),
          maxUnits: f.maxUnits ?? undefined,
          allergens: (f.allergens || '').split(',').map((s: string) => s.trim()).filter(Boolean),
          dietaryInfo: (f.dietaryInfo || '').split(',').map((s: string) => s.trim()).filter(Boolean),
          isActive: !!f.isActive,
          createdAt: f.createdAt,
          updatedAt: f.updatedAt,
        };
      });
      store.setFoods(mapped as any);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createFood = useCallback(async (foodData: any) => {
    setIsLoading(true);
    clearError();
    try {
      const category = String(foodData.category || 'other').toLowerCase();
      const backendCategory =
        category === 'beverages' ? 'BEVERAGE' :
        category === 'desserts' ? 'DESSERT' :
        category === 'appetizer' ? 'APPETIZER' :
        category === 'special' ? 'SPECIAL' :
        'MAIN';
      const payload = {
        name: foodData.name,
        description: foodData.description,
        category: backendCategory,
        basePrice: Number(foodData.basePrice ?? foodData.pricePerUnit ?? foodData.pricePerGuest ?? 0),
        baseCost: Number(foodData.baseCost ?? 0),
        servingSize: foodData.unit || 'unit',
        guestsPerUnit: Number(foodData.guestsPerUnit ?? 1),
        maxUnits: foodData.maxUnits ?? undefined,
        allergens: (foodData.allergens || []).join(', '),
        dietaryInfo: (foodData.dietaryInfo || []).join(', '),
        isActive: foodData.isActive ?? true,
        isTemplate: false,
        createdAt: foodData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.foodItems.createFoodItem(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        category: category,
        basePrice: Number((response as any).basePrice ?? 0),
        pricePerUnit: Number((response as any).basePrice ?? 0),
        pricePerGuest: Number((response as any).basePrice ?? 0),
        unit: (response as any).servingSize || 'unit',
        guestsPerUnit: Number((response as any).guestsPerUnit ?? 1),
        maxUnits: (response as any).maxUnits ?? undefined,
        allergens: ((response as any).allergens || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        dietaryInfo: ((response as any).dietaryInfo || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.addFood(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateFood = useCallback(async (id: string, foodData: any) => {
    setIsLoading(true);
    clearError();
    try {
      const category = String(foodData.category || 'other').toLowerCase();
      const backendCategory =
        category === 'beverages' ? 'BEVERAGE' :
        category === 'desserts' ? 'DESSERT' :
        category === 'appetizer' ? 'APPETIZER' :
        category === 'special' ? 'SPECIAL' :
        'MAIN';
      const payload = {
        id: Number(id),
        name: foodData.name,
        description: foodData.description,
        category: backendCategory,
        basePrice: Number(foodData.basePrice ?? foodData.pricePerUnit ?? foodData.pricePerGuest ?? 0),
        baseCost: Number(foodData.baseCost ?? 0),
        servingSize: foodData.unit || 'unit',
        guestsPerUnit: Number(foodData.guestsPerUnit ?? 1),
        maxUnits: foodData.maxUnits ?? undefined,
        allergens: (foodData.allergens || []).join(', '),
        dietaryInfo: (foodData.dietaryInfo || []).join(', '),
        isActive: foodData.isActive ?? true,
        isTemplate: false,
        createdAt: foodData.createdAt || new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      } as any;
      const response = await apiServices.foodItems.updateFoodItem(payload);
      const mapped = {
        id: String((response as any).id),
        name: (response as any).name,
        description: (response as any).description,
        category: category,
        basePrice: Number((response as any).basePrice ?? 0),
        pricePerUnit: Number((response as any).basePrice ?? 0),
        pricePerGuest: Number((response as any).basePrice ?? 0),
        unit: (response as any).servingSize || 'unit',
        guestsPerUnit: Number((response as any).guestsPerUnit ?? 1),
        maxUnits: (response as any).maxUnits ?? undefined,
        allergens: ((response as any).allergens || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        dietaryInfo: ((response as any).dietaryInfo || '').split(',').map((s: string) => s.trim()).filter(Boolean),
        isActive: !!(response as any).isActive,
        createdAt: (response as any).createdAt,
        updatedAt: (response as any).updatedAt,
      } as any;
      store.updateFood(mapped);
      return mapped;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteFood = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    try {
      await apiServices.foodItems.deleteFoodItem(Number(id) as any);
      store.deleteFood(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Transport Management
  const loadTransports = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.getTransports(params);
      store.setTransportTemplates(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createTransport = useCallback(async (transportData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.createTransport(transportData);
      store.addTransportTemplate(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateTransport = useCallback(async (id: string, transportData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.updateTransport({ ...transportData, id });
      store.updateTransportTemplate(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteTransport = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.transports.deleteTransport(id);
      store.deleteTransportTemplate(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Task Management
  const loadTasks = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.getTasks(params);
      store.setTasks(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createTask = useCallback(async (taskData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.createTask(taskData);
      store.addTask(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateTask = useCallback(async (id: string, taskData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.updateTask({ ...taskData, id });
      store.updateTask(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteTask = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.tasks.deleteTask(id);
      store.deleteTask(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Notification Management
  const loadNotifications = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.notifications.getNotifications(params);
      // Update notifications in store (you'll need to add this to your store)
      return response.data;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [handleError, clearError]);

  const markNotificationAsRead = useCallback(async (id: string) => {
    try {
      await apiServices.notifications.markAsRead(id);
      store.markNotificationRead(parseInt(id));
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    }
  }, [store, handleError]);

  // Analytics
  const loadDashboardStats = useCallback(async () => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.analytics.getDashboardStats();
      return response.data;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [handleError, clearError]);

  // Initialize data on mount
  useEffect(() => {
    const initializeData = async () => {
      try {
        if (authService.isAuthenticated()) {
          // Ensure current user is populated on hard refresh
          if (!store.currentUser || !(store.currentUser as any).id) {
            try {
              const user = await authService.getCurrentUser();
              store.setCurrentUser({
                id: user?.id,
                name: `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.login,
                email: user?.email,
                role: (user?.authorities?.includes('ROLE_ADMIN') ? 'admin' : 'sales') as any,
              } as any);
            } catch (e) {
              // If token invalid, clear and bail
              localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
              return;
            }
          }

          await Promise.all([
            loadBudgets(),
            loadActivities(),
            loadAccommodations(),
            loadMenus(),
            loadFoods(),
            loadProducts(),
            loadTransports(),
            loadTasks(),
            loadNotifications(),
          ]);
        }
      } catch (error) {
        console.error('Error initializing data:', error);
      }
    };

    initializeData();
  }, [loadBudgets, loadActivities, loadAccommodations, loadMenus, loadFoods, loadProducts, loadTransports, loadTasks, loadNotifications, store.currentUser]);

  return {
    // State
    isLoading,
    error,
    clearError,
    
    // Authentication
    login,
    logout,
    
    // Budget Management
    loadBudgets,
    createBudget,
    updateBudget,
    deleteBudget,
    
    // Activity Management
    loadActivities,
    createActivity,
    updateActivity,
    deleteActivity,
    
    // Accommodation Management
    loadAccommodations,
    createAccommodation,
    updateAccommodation,
    deleteAccommodation,
    
    // Menu Management
    loadMenus,
    createMenu,
    updateMenu,
    deleteMenu,
    
    // Product Management
    loadProducts,
    createProduct,
    updateProduct,
    deleteProduct,

    // Food Management
    loadFoods,
    createFood,
    updateFood,
    deleteFood,
    
    // Transport Management
    loadTransports,
    createTransport,
    updateTransport,
    deleteTransport,
    
    // Task Management
    loadTasks,
    createTask,
    updateTask,
    deleteTask,
    
    // Notification Management
    loadNotifications,
    markNotificationAsRead,
    
    // Analytics
    loadDashboardStats,
  };
}; 