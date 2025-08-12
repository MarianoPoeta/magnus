import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useStore } from '../store';
import { useAuth } from '../hooks/useAuth';
import { Button } from '../components/ui/button';
import { Shield, DollarSign, Truck, UtensilsCrossed, Sparkles, ArrowRight } from 'lucide-react';
import { Card } from '../components/ui/card';
import { LoadingSpinner } from '../components/LoadingSpinner';

const roleIcons = {
  admin: Shield,
  sales: DollarSign,
  logistics: Truck,
  cook: UtensilsCrossed,
};

const roleColors = {
  admin: 'bg-purple-50 text-purple-700 border-purple-200 hover:bg-purple-100',
  sales: 'bg-blue-50 text-blue-700 border-blue-200 hover:bg-blue-100',
  logistics: 'bg-green-50 text-green-700 border-green-200 hover:bg-green-100',
  cook: 'bg-orange-50 text-orange-700 border-orange-200 hover:bg-orange-100',
};

const roleDescriptions = {
  admin: 'Acceso completo al sistema y configuración',
  sales: 'Gestión de presupuestos y coordinación con clientes',
  logistics: 'Planificación de transporte y alojamiento',
  cook: 'Planificación de menús y preparación de alimentos',
};

const roleNames = {
  admin: 'Administrador',
  sales: 'Ventas',
  logistics: 'Logística',
  cook: 'Cocinero',
};

const Login: React.FC = () => {
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('admin');
  const [isLoading, setIsLoading] = useState(false);
  const { setCurrentUser } = useStore();
  const { login, error } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async () => {
    setIsLoading(true);
    try {
      const result = await login({ username, password, rememberMe: true });
      if ((result as any).success) {
        navigate('/dashboard', { replace: true });
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 flex items-center justify-center p-4">
      <Card className="w-full max-w-2xl p-8 shadow-xl">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center mb-4">
            <Sparkles className="h-12 w-12 text-purple-600 mr-3" />
            <h1 className="text-4xl font-bold text-slate-800">MAGNUS</h1>
          </div>
          <p className="text-slate-600 text-lg">
            Sistema de gestión de eventos y presupuestos
          </p>
        </div>

        <div className="space-y-6">
          <div>
            <h2 className="text-xl font-semibold text-slate-800 mb-4 text-center">
              Inicia sesión
            </h2>
            <div className="space-y-4 max-w-md mx-auto">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Usuario</label>
                <input
                  className="w-full border rounded px-3 py-2 focus:outline-none focus:ring"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="admin"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Contraseña</label>
                <input
                  className="w-full border rounded px-3 py-2 focus:outline-none focus:ring"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="admin"
                />
              </div>
              {error && (
                <div className="text-sm text-red-600">{error}</div>
              )}
            </div>
          </div>

          <Button
            onClick={handleLogin}
            disabled={isLoading}
            className="w-full bg-purple-600 hover:bg-purple-700 text-white py-3 text-lg"
            size="lg"
          >
            {isLoading ? (
              <LoadingSpinner size="sm" text="Iniciando sesión..." />
            ) : (
              <>
                Ingresar
                <ArrowRight className="h-5 w-5 ml-2" />
              </>
            )}
          </Button>

          <div className="text-center text-sm text-slate-500">
            <p>Usa admin/admin o user/user (JHipster por defecto)</p>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default Login; 