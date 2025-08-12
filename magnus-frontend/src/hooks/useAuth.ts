import { useState, useEffect, useCallback } from 'react';
import { authService } from '../services/api';

interface User {
  id?: number;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  imageUrl?: string;
  createdDate?: string;
  lastModifiedDate?: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

interface LoginCredentials {
  username: string;
  password: string;
  rememberMe?: boolean;
}

interface RegisterData {
  login: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
  langKey?: string;
}

export const useAuth = () => {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    isAuthenticated: false,
    isLoading: true,
    error: null,
  });

  // Initialize authentication state
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        if (authService.isAuthenticated()) {
          const user = await authService.getCurrentUser();
          setAuthState({
            user,
            isAuthenticated: true,
            isLoading: false,
            error: null,
          });
        } else {
          setAuthState({
            user: null,
            isAuthenticated: false,
            isLoading: false,
            error: null,
          });
        }
      } catch (error) {
        console.error('Failed to initialize authentication:', error);
        setAuthState({
          user: null,
          isAuthenticated: false,
          isLoading: false,
          error: 'Failed to initialize authentication',
        });
      }
    };

    initializeAuth();
  }, []);

  // Login function
  const login = useCallback(async (credentials: LoginCredentials) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      const response = await authService.login(credentials);
      
      if (response.id_token) {
        // Get user info after successful login
        const user = await authService.getCurrentUser();
        setAuthState({
          user,
          isAuthenticated: true,
          isLoading: false,
          error: null,
        });
        return { success: true, user };
      } else {
        throw new Error('No token received');
      }
    } catch (error: any) {
      const errorMessage = error.message || 'Login failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Register function
  const register = useCallback(async (userData: RegisterData) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      await authService.register(userData);
      
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, message: 'Registration successful. Please check your email to activate your account.' };
    } catch (error: any) {
      const errorMessage = error.message || 'Registration failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Logout function
  const logout = useCallback(async () => {
    try {
      await authService.logout();
      setAuthState({
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,
      });
      return { success: true };
    } catch (error: any) {
      console.error('Logout error:', error);
      // Even if logout fails, clear local state
      setAuthState({
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,
      });
      return { success: true };
    }
  }, []);

  // Update account function
  const updateAccount = useCallback(async (userData: Partial<User>) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      const updatedUser = await authService.updateAccount(userData);
      
      setAuthState(prev => ({
        ...prev,
        user: updatedUser,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, user: updatedUser };
    } catch (error: any) {
      const errorMessage = error.message || 'Account update failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Change password function
  const changePassword = useCallback(async (currentPassword: string, newPassword: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      await authService.changePassword({ currentPassword, newPassword });
      
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, message: 'Password changed successfully' };
    } catch (error: any) {
      const errorMessage = error.message || 'Password change failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Reset password init function
  const resetPasswordInit = useCallback(async (email: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      await authService.resetPasswordInit(email);
      
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, message: 'Password reset email sent' };
    } catch (error: any) {
      const errorMessage = error.message || 'Password reset failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Reset password finish function
  const resetPasswordFinish = useCallback(async (key: string, newPassword: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      await authService.resetPasswordFinish(key, newPassword);
      
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, message: 'Password reset successful' };
    } catch (error: any) {
      const errorMessage = error.message || 'Password reset failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Activate account function
  const activateAccount = useCallback(async (key: string) => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true, error: null }));
      
      await authService.activateAccount(key);
      
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: null,
      }));
      
      return { success: true, message: 'Account activated successfully' };
    } catch (error: any) {
      const errorMessage = error.message || 'Account activation failed';
      setAuthState(prev => ({
        ...prev,
        isLoading: false,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Refresh user data
  const refreshUser = useCallback(async () => {
    try {
      if (authService.isAuthenticated()) {
        const user = await authService.getCurrentUser();
        setAuthState(prev => ({
          ...prev,
          user,
          isAuthenticated: true,
          error: null,
        }));
        return { success: true, user };
      }
      return { success: false, error: 'Not authenticated' };
    } catch (error: any) {
      const errorMessage = error.message || 'Failed to refresh user data';
      setAuthState(prev => ({
        ...prev,
        error: errorMessage,
      }));
      return { success: false, error: errorMessage };
    }
  }, []);

  // Clear error
  const clearError = useCallback(() => {
    setAuthState(prev => ({ ...prev, error: null }));
  }, []);

  // Check if user has authority
  const hasAuthority = useCallback((authority: string) => {
    return authState.user?.authorities?.includes(authority) || false;
  }, [authState.user?.authorities]);

  // Check if user is admin
  const isAdmin = useCallback(() => {
    return hasAuthority('ROLE_ADMIN');
  }, [hasAuthority]);

  return {
    // State
    user: authState.user,
    isAuthenticated: authState.isAuthenticated,
    isLoading: authState.isLoading,
    error: authState.error,
    
    // Functions
    login,
    register,
    logout,
    updateAccount,
    changePassword,
    resetPasswordInit,
    resetPasswordFinish,
    activateAccount,
    refreshUser,
    clearError,
    hasAuthority,
    isAdmin,
  };
}; 